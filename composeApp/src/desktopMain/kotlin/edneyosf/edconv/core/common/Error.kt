package edneyosf.edconv.core.common

enum class Error(val code: String) {
    DEFAULT(code = "0000"),
    LOAD_CONFIGS(code = "0001"),
    UNKNOWN_INPUT_MEDIA(code = "0002"),
    NO_DURATION_INPUT_MEDIA(code = "0003"),
    NO_CHANNELS_INPUT_MEDIA(code = "0004"),
    NO_RESOLUTION_INPUT_MEDIA(code = "0005"),
    NO_STREAM_FOUND_INPUT_MEDIA(code = "0006"),
    FFMPEG_OR_FFPROBE_VERIFICATION(code = "0007"),
    CONFIGURATION_SAVE(code = "0008"),
    ON_STARTING_CONVERSION(code = "0009"),
    ON_STARTING_CONVERSION_REQUIREMENTS(code = "000A"),
    ON_STOPPING_CONVERSION(code = "000B"),
    START_TIME_NULL(code = "000C"),
    INPUT_FILE_NOT_EXIST(code = "000D"),
    INPUT_NOT_FILE(code = "000E"),
    CONVERSION_PROCESS_COMPLETED(code = "000F"),
    PROCESS_NULL(code = "000G"),
    CONVERSION_PROCESS(code = "000H"),
    VMAF_PROCESS_COMPLETED(code = "000I"),
    VMAF_PROCESS(code = "000J"),
    VMAF_SCORE_NULL(code = "000K"),
    ON_STOPPING_VMAF(code = "000L"),
    ON_STARTING_VMAF_REQUIREMENTS(code = "000M"),
    NO_VIDEO_INPUT_MEDIA(code = "000N"),
    VMAF_MODEL_SAVE(code = "000O");
}