package a.exam.coresdk.model


enum class AdSize(val width: Int, val height: Int) {
    BANNER(320, 50),
    BANNER_2(320, 100),
    BANNER_3(300, 250);

    companion object {
        fun getWidth(name: AdSize): Int {
            return name.width
        }

        fun getHeight(name: AdSize): Int {
            return name.height
        }
    }
}
