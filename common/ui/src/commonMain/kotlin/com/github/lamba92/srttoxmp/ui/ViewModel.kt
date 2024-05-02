package com.github.lamba92.srttoxmp.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import com.github.lamba92.srttoexif.AppPath
import com.github.lamba92.srttoexif.MetadataPatcher
import com.github.lamba92.srttoexif.parseMetadataFromSRT
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


enum class FileSorting { NAME, DATE, SIZE }

class ViewModel(
    private val viewModelScope: CoroutineScope,
    private val patcher: MetadataPatcher,
    private val cachePath: AppPath.Directory,
) {

    companion object {
        val prettyJson = Json { prettyPrint = true }
    }

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized = _isInitialized.asStateFlow()

    private val _patchingState = MutableStateFlow<Double?>(null)
    val patchingState = _patchingState.asStateFlow()

    val drawerState = DrawerState(DrawerValue.Closed)
    private val _groupByType = MutableStateFlow(true)
    private val _sortedBy = MutableStateFlow(FileSorting.DATE)
    private val _isSortingReversed = MutableStateFlow(false)

    val isSortingReversed = _isSortingReversed.asStateFlow()
    val sortedBy = _sortedBy.asStateFlow()
    val groupByType = _groupByType.asStateFlow()

    private val _recentPaths = MutableStateFlow<List<AppPath.Directory>>(emptyList())
    private val _selectedDirectory = MutableStateFlow<AppPath.Directory?>(null)
    private val _selectedFile = MutableStateFlow<AppPath.File?>(null)

    val recentPaths = _recentPaths.asStateFlow()
    val selectedDirectory = _selectedDirectory.asStateFlow()
    val selectedFile = _selectedFile.asStateFlow()

    val appFiles = MutableStateFlow<FileLoadingResult?>(null)

    val images = appFiles
        .filterIsInstance<FileLoadingResult.Loaded>()
        .map { it.files.filterIsInstance<AppFile.Multimedia.Image>() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val videos = appFiles
        .filterIsInstance<FileLoadingResult.Loaded>()
        .map { it.files.filterIsInstance<AppFile.Multimedia.Video>() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val srts = appFiles
        .filterIsInstance<FileLoadingResult.Loaded>()
        .map { it.files.filterIsInstance<AppFile.Srt>() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val directories = appFiles
        .filterIsInstance<FileLoadingResult.Loaded>()
        .map { it.files.filterIsInstance<AppFile.Directory>() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch { loadCache() }
        selectedDirectory.filterNotNull()
            .onEach { path -> loadFiles(path) }
            .retry { cause ->
                println(cause)
                true
            }
            .launchIn(viewModelScope)

        appCacheFlow
            .debounce(5.seconds)
            .onEach {
                cachePath.resolve("cache.json")
                    .let { it as? AppPath.File }
                    ?.writeText(prettyJson.encodeToString(it))
            }
            .launchIn(viewModelScope)
    }

    private val appCacheFlow
        get() = combine(
            isSortingReversed,
            sortedBy,
            groupByType
        ) { isSortingReversed, sortedBy, groupByType ->
            AppCache(
                groupByType = groupByType,
                sortedBy = sortedBy,
                isSortingReversed = isSortingReversed,
                recentPaths = recentPaths.value.map { it.pathString }
            )
        }

    private suspend fun loadCache() {
        val settings = cachePath.listFiles()
            .filterIsInstance<AppPath.File>()
            .firstOrNull { it.name == "cache.json" }
            ?.let { Json.decodeFromString<AppCache>(it.readText()) }

        if (settings != null) {
            val paths = settings.recentPaths
                .map { AppPath(it) }
                .filterIsInstance<AppPath.Directory>()
            _recentPaths.emit(paths)
            _selectedDirectory.emit(paths.firstOrNull())
            _isSortingReversed.emit(settings.isSortingReversed)
            _sortedBy.emit(settings.sortedBy)
            _groupByType.emit(settings.groupByType)
        }
        _isInitialized.emit(true)
    }

    private suspend fun loadFiles(path: AppPath.Directory) {
        val files = path.listFiles()

        val srtsMap = files.filterIsInstance<AppPath.File>()
            .filter { it.extension == "srt" }
            .associateBy { it.nameWithoutExtension }

        val appFiles = files
            .mapNotNull {
                when (it) {
                    is AppPath.File -> when (it.extension) {
                        in imagesExtensions -> AppFile.Multimedia.Image(
                            path = it,
                            location = patcher.readMetadata(it),
                            hasSrtFile = srtsMap.containsKey(it.nameWithoutExtension)
                        )

                        in videosExtensions -> AppFile.Multimedia.Video(
                            path = it,
                            location = patcher.readMetadata(it),
                            hasSrtFile = srtsMap.containsKey(it.nameWithoutExtension)
                        )

                        "srt" -> AppFile.Srt(it)
                        else -> null
                    }

                    is AppPath.Directory -> AppFile.Directory(it)
                }
            }

        this.appFiles.emit(FileLoadingResult.Loaded(appFiles))
    }

    private fun <T> bringToFront(list: List<T>, element: T): List<T> {
        val mutableList = list.toMutableList()
        mutableList.remove(element)
        mutableList.add(0, element)
        return mutableList.toList().take(10)
    }

    fun selectFile(file: AppPath.File) {
        viewModelScope.launch {
            _selectedFile.emit(file)
        }
    }

    fun toggleDrawer() {
        viewModelScope.launch {
            when (drawerState.currentValue) {
                DrawerValue.Closed -> drawerState.open()
                DrawerValue.Open -> drawerState.close()
            }
        }
    }

    fun selectPath(path: AppPath.Directory) {
        viewModelScope.launch {
            _recentPaths.emit(bringToFront(recentPaths.value, path))
            _selectedDirectory.emit(path)
        }
    }

    fun toggleSortingReversed() {
        viewModelScope.launch {
            _isSortingReversed.emit(!isSortingReversed.value)
        }
    }

    fun setSorting(sorting: FileSorting) {
        viewModelScope.launch {
            _sortedBy.emit(sorting)
        }
    }

    fun toggleGroupByType() {
        viewModelScope.launch {
            _groupByType.emit(!groupByType.value)
        }
    }

    private var patchingJob: Job? = null

    fun cancelPatching() {
        patchingJob?.cancel()
        patchingJob = null
    }

    fun patchMetadata() {
        patchingJob = viewModelScope.launch {
            val srtsMap = srts.first()
                .associateBy { it.path.nameWithoutExtension }

            val medias = appFiles.filterIsInstance<FileLoadingResult.Loaded>()
                .flatMapConcat { it.files.asFlow() }
                .filterIsInstance<AppFile.Multimedia>()
                .filter { it.location == null && it.path.nameWithoutExtension in srtsMap }
                .toList()

            _patchingState.emit(0.0)

            medias.forEachIndexed { index, media ->
                val coordinates = srtsMap
                    .getValue(media.path.nameWithoutExtension)
                    .path
                    .readLines()
                    .filter { "latitude" in it && "longitude" in it }
                    .map { parseMetadataFromSRT(it) }
                    .firstOrNull()
                if (coordinates != null) {
                    patcher.patchMetadata(media.path, coordinates)
                }
                _patchingState.emit((index + 1).toDouble() / medias.size)
            }

        }
    }

}