package com.inverce.mod.v2.processing

class Job<T, R>(
        val processor: JobProcessor<T, R>,
        val steps: List<T>
) {
    val results: List<R> = emptyList()


}


class Processors {
    fun <T, R> create(process: (item: T, context: JobContext<T, R>) -> R) {


    }


}

typealias JobProcessor<T, R> = (item: T, context: JobContext<T, R>) -> R


val pr: JobProcessor<Int, Double> = { i, c -> i.toLong() }
                .then { i, c -> i.toDouble()

                }


                .then(object : JobProcessor<Long, Double> {
                    override fun process(context: JobContext<Long, Double>, item: Long): Double {
                        return item.toDouble()
                    }
                })

fun <T, B, R> JobProcessor<T, B>.then(processor: JobProcessor<B, R>): JobProcessor<T, R> {

}

interface JobContext<T, R> {
    fun reportProgress(progress: Float)
    fun reportResult(result: R)
    fun reportResult(result: Exception)
}

class JobResult<ITEM, RESULT>(
        val job: Job<ITEM, RESULT>,
        val result: RESULT?,
        val exception: Exception?)
