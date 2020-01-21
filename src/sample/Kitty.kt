package sample

import org.junit.Test

class Kitty {

    class P(var name: String) {
        val id = 555
    }

    fun a(x: Int, v: P.() -> Unit) {
        println(x)
        println(v.apply { })
    }

    @Test
    fun b() {
        println("id")
        a(2, v = { P("d") })
        println("123")
    }
}
