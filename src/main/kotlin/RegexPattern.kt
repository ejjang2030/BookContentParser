object RegexPattern {
    val jeNumberPyeon = Regex("^제[0-9]+편")
    val numberDotNumber = Regex("^[0-9]+.[0-9]+")
    val numberDotNumberDotNumber = Regex("^[0-9]+.[0-9]+.[0-9]+")
    val burok = Regex("^부록")
    val jeJang = Regex("^제[0-9]+장")
    val pyeon = Regex("^[가-힣]+편")
    val numberJang = Regex("^[0-9]+장")
    val numberSpaceJang = Regex("^[0-9]+ 장")
    val jeBu = Regex("^제[0-9]+부")
    val partSpaceNumber = Regex("^Part [0-9]+")
    val PARTSpaceNumber = Regex("^PART [0-9]+")
    val number = Regex("^[0-9]+")
    val CHAPTERSpaceNumber = Regex("^CHAPTER [0-9]+")
    val ChapterSpaceNumber = Regex("^Chapter [0-9]+")
    val nothing = Regex("^[가-힣a-zA-Z]+")
    val numberBarNumber = Regex("^[0-9]+-[0-9]+")
    val underbar2 = Regex("^_{2}")
    val underbar4 = Regex("^_{4}")
    val brackets = Regex("^\\[[가-힣]+\\]")
    val burokAlphabet = Regex("^부록 [A-Za-z]")
    val CHAPTERNumber = Regex("^CHAPTER[0-9]+")
    val ChapterNumber = Regex("^Chapter[0-9]+")
    val numberDot = Regex("^[0-9]+.")
    val PartNumber = Regex("^Part[0-9]+")
    val PARTNumber = Regex("^PART[0-9]+")
    val jjaeMadang = Regex("^[가-힣]+째마당")
    val underbar1 = Regex("^_")
    val contentTokens = mapOf(
        jeNumberPyeon to "제00편",
        jeJang to "제00장",
        pyeon to "몇째편",
        jeBu to "제00부",
        jjaeMadang to "몇째마당",
        partSpaceNumber to "Part 00",
        PARTSpaceNumber to "PART 00",
        PartNumber to "Part00",
        PARTNumber to "PART00",
        numberSpaceJang to "00 장",
        numberJang to "00장",
        numberBarNumber to "00-00",
        numberDotNumber to "00.00",
        numberDotNumberDotNumber to "00.00.00",
        number to "00",
        numberDot to "00.",
        CHAPTERSpaceNumber to "CHAPTER 00",
        ChapterSpaceNumber to "Chapter 00",
        CHAPTERNumber to "CHAPTER00",
        ChapterNumber to "Chapter00",
        underbar4 to "____",
        underbar2 to "__",
        underbar1 to "_",
        burokAlphabet to "부록 A",
        burok to "부록",
        brackets to "[소제목]",
        nothing to "소제목"
    )
}