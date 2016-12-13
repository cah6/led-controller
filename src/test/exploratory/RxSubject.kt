package exploratory

import org.junit.Test
import rx.subjects.PublishSubject
import rx.subjects.Subject

/**
 * Created by christian.henry on 11/22/16.
 */
class RxSubject {

    @Test
    fun test() {
        val subject: Subject<Int, Int> = PublishSubject.create()

        subject.onNext(1)
        subject.onNext(2)
        subject.onNext(3)
        subject.subscribe(::println)
        subject.onNext(4)
        subject.onNext(5)
        subject.onNext(6)
    }
}