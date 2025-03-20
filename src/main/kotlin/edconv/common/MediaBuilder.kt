package edconv.common

import edconv.core.EdconvArgs

abstract class MediaBuilder(
    protected open val inputFile: String,
    protected open val outputFile: String
) {
    protected val cmd = mutableListOf<String>()

    fun build(): List<String> {
        cmd.add(EdconvArgs.INPUT)
        cmd.add(inputFile)

        cmd.add(EdconvArgs.OUTPUT)
        cmd.add(outputFile)

        return cmd
    }
}