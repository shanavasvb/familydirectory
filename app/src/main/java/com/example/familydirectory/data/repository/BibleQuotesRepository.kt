package com.example.familydirectory.data.repository

import com.example.familydirectory.data.model.BibleQuote

object BibleQuotesRepository {

    private val quotes = listOf(
        BibleQuote(
            malayalamText = "നിങ്ങളുടെ ഹൃദയം കലങ്ങരുത്; ദൈവത്തിൽ വിശ്വസിക്കുവിൻ; എന്നിലും വിശ്വസിക്കുവിൻ.",
            englishText = "Let not your heart be troubled: ye believe in God, believe also in me.",
            reference = "John 14:1",
            malayalamReference = "യോഹന്നാൻ 14:1",
            dayOfYear = 1
        ),
        BibleQuote(
            malayalamText = "ദൈവം സ്നേഹമാകുന്നു; സ്നേഹത്തിൽ വസിക്കുന്നവൻ ദൈവത്തിൽ വസിക്കുന്നു; ദൈവവും അവനിൽ വസിക്കുന്നു.",
            englishText = "God is love; and he that dwelleth in love dwelleth in God, and God in him.",
            reference = "1 John 4:16",
            malayalamReference = "1 യോഹന്നാൻ 4:16",
            dayOfYear = 2
        ),
        BibleQuote(
            malayalamText = "കർത്താവിൽ സന്തോഷിപ്പിൻ എപ്പോഴും; വീണ്ടും പറയുന്നു, സന്തോഷിപ്പിൻ.",
            englishText = "Rejoice in the Lord always: and again I say, Rejoice.",
            reference = "Philippians 4:4",
            malayalamReference = "ഫിലിപ്പിയർ 4:4",
            dayOfYear = 3
        ),
        BibleQuote(
            malayalamText = "യഹോവയിൽ ആശ്രയിച്ചു നന്മ ചെയ്യുക; ദേശത്തു പാർത്തു വിശ്വസ്തതയെ മേയുക.",
            englishText = "Trust in the LORD, and do good; so shalt thou dwell in the land, and verily thou shalt be fed.",
            reference = "Psalm 37:3",
            malayalamReference = "സങ്കീർത്തനം 37:3",
            dayOfYear = 4
        ),
        BibleQuote(
            malayalamText = "നിങ്ങളുടെ വഴി യഹോവയ്ക്കു ഭരമേല്പിക്കുക; അവനിൽ ആശ്രയിക്കുക; അവൻ നിവർത്തിക്കും.",
            englishText = "Commit thy way unto the LORD; trust also in him; and he shall bring it to pass.",
            reference = "Psalm 37:5",
            malayalamReference = "സങ്കീർത്തനം 37:5",
            dayOfYear = 5
        ),
        BibleQuote(
            malayalamText = "യഹോവയെ അന്വേഷിപ്പിൻ; അവനെ കണ്ടെത്താവുന്നേടത്തോളം വിളിച്ചപേക്ഷിപ്പിൻ; അവൻ അടുത്തിരിക്കുമ്പോൾ തന്നേ.",
            englishText = "Seek ye the LORD while he may be found, call ye upon him while he is near.",
            reference = "Isaiah 55:6",
            malayalamReference = "യെശയ്യാ 55:6",
            dayOfYear = 6
        ),
        BibleQuote(
            malayalamText = "എന്റെ ആത്മാവേ, യഹോവയെ വാഴ്ത്തുക; എന്റെ അന്തരംഗമേ, അവന്റെ വിശുദ്ധനാമത്തെ വാഴ്ത്തുക.",
            englishText = "Bless the LORD, O my soul: and all that is within me, bless his holy name.",
            reference = "Psalm 103:1",
            malayalamReference = "സങ്കീർത്തനം 103:1",
            dayOfYear = 7
        ),
        BibleQuote(
            malayalamText = "സ്നേഹം ദീർഘക്ഷമയുള്ളതു; ദയയുള്ളതും; സ്നേഹം അസൂയപ്പെടുന്നില്ല; സ്നേഹം വമ്പു പറയുന്നില്ല.",
            englishText = "Love is patient, love is kind. It does not envy, it does not boast.",
            reference = "1 Corinthians 13:4",
            malayalamReference = "1 കൊരിന്ത്യർ 13:4",
            dayOfYear = 8
        ),
        BibleQuote(
            malayalamText = "നിങ്ങൾ എന്റെ വചനത്തിൽ വസിച്ചാൽ സാക്ഷാൽ എന്റെ ശിഷ്യന്മാർ; സത്യം അറിയും; സത്യം നിങ്ങളെ സ്വതന്ത്രരാക്കും.",
            englishText = "If you hold to my teaching, you are really my disciples. Then you will know the truth, and the truth will set you free.",
            reference = "John 8:31-32",
            malayalamReference = "യോഹന്നാൻ 8:31-32",
            dayOfYear = 9
        ),
        BibleQuote(
            malayalamText = "യഹോവ എന്റെ ഇടയൻ; എനിക്കു മുട്ടുണ്ടാകയില്ല.",
            englishText = "The LORD is my shepherd; I shall not want.",
            reference = "Psalm 23:1",
            malayalamReference = "സങ്കീർത്തനം 23:1",
            dayOfYear = 10
        ),
        BibleQuote(
            malayalamText = "നിങ്ങളുടെ ഭാരമൊക്കെയും അവങ്കൽ വെച്ചുകളവിൻ; അവൻ നിങ്ങളെക്കുറിച്ചു കരുതലുള്ളവൻ ആകുന്നു.",
            englishText = "Cast all your anxiety on him because he cares for you.",
            reference = "1 Peter 5:7",
            malayalamReference = "1 പത്രൊസ് 5:7",
            dayOfYear = 11
        ),
        BibleQuote(
            malayalamText = "കർത്താവിന്റെ കൃപയെ രുചിച്ചുനോക്കുവിൻ; അവങ്കൽ ശരണപ്പെടുന്ന പുരുഷൻ ഭാഗ്യവാൻ.",
            englishText = "Taste and see that the LORD is good; blessed is the one who takes refuge in him.",
            reference = "Psalm 34:8",
            malayalamReference = "സങ്കീർത്തനം 34:8",
            dayOfYear = 12
        ),
        BibleQuote(
            malayalamText = "നിങ്ങൾ എന്നെ വിളിച്ചു പ്രാർത്ഥിക്കയും എന്റെ അടുക്കൽ വന്നു എന്നോടു അപേക്ഷിക്കയും ചെയ്താൽ ഞാൻ നിങ്ങൾക്കു ഉത്തരം അരുളും.",
            englishText = "Then you will call on me and come and pray to me, and I will listen to you.",
            reference = "Jeremiah 29:12",
            malayalamReference = "യിരെമ്യാവു 29:12",
            dayOfYear = 13
        ),
        BibleQuote(
            malayalamText = "യഹോവയുടെ ന്യായപ്രമാണം തികഞ്ഞതും പ്രാണനെ തണുപ്പിക്കുന്നതും ആകുന്നു.",
            englishText = "The law of the LORD is perfect, refreshing the soul.",
            reference = "Psalm 19:7",
            malayalamReference = "സങ്കീർത്തനം 19:7",
            dayOfYear = 14
        ),
        BibleQuote(
            malayalamText = "വഴിയും സത്യവും ജീവനും ഞാൻ ആകുന്നു; എൻമുഖാന്തരം അല്ലാതെ ആരും പിതാവിന്റെ അടുക്കൽ വരുന്നില്ല.",
            englishText = "I am the way and the truth and the life. No one comes to the Father except through me.",
            reference = "John 14:6",
            malayalamReference = "യോഹന്നാൻ 14:6",
            dayOfYear = 15
        ),
        BibleQuote(
            malayalamText = "നിങ്ങൾക്കു വേണ്ടുന്നതു ഒക്കെയും യാചിപ്പിൻ; അത് നിങ്ങൾക്കു ലഭിക്കും.",
            englishText = "Ask and it will be given to you; seek and you will find.",
            reference = "Matthew 7:7",
            malayalamReference = "മത്തായി 7:7",
            dayOfYear = 16
        ),
        BibleQuote(
            malayalamText = "നന്മ ചെയ്തു തളരരുതു; തളരാതെയിരുന്നാൽ തക്ക സമയത്തു നാം കൊയ്യും.",
            englishText = "Let us not become weary in doing good, for at the proper time we will reap a harvest if we do not give up.",
            reference = "Galatians 6:9",
            malayalamReference = "ഗലാത്യർ 6:9",
            dayOfYear = 17
        ),
        BibleQuote(
            malayalamText = "കർത്താവു എന്റെ വെളിച്ചവും രക്ഷയും; ഞാൻ ആരെ ഭയപ്പെടും? കർത്താവു എന്റെ ജീവന്റെ ദുർഗ്ഗം; ഞാൻ ആരെ പേടിക്കും?",
            englishText = "The LORD is my light and my salvation—whom shall I fear? The LORD is the stronghold of my life—of whom shall I be afraid?",
            reference = "Psalm 27:1",
            malayalamReference = "സങ്കീർത്തനം 27:1",
            dayOfYear = 18
        ),
        BibleQuote(
            malayalamText = "അഹങ്കാരത്തോടു കൂടെ അഴിമതി വരുന്നു; വിനയമുള്ളവരോടു കൂടെയോ ജ്ഞാനം.",
            englishText = "When pride comes, then comes disgrace, but with humility comes wisdom.",
            reference = "Proverbs 11:2",
            malayalamReference = "സദൃശവാക്യങ്ങൾ 11:2",
            dayOfYear = 19
        ),
        BibleQuote(
            malayalamText = "നിന്റെ പ്രവൃത്തികളെ യഹോവയ്ക്കു സമർപ്പിക്ക; നിന്റെ നിരൂപണങ്ങൾ സാധിക്കും.",
            englishText = "Commit to the LORD whatever you do, and he will establish your plans.",
            reference = "Proverbs 16:3",
            malayalamReference = "സദൃശവാക്യങ്ങൾ 16:3",
            dayOfYear = 20
        ),
        BibleQuote(
            malayalamText = "നീ ബലഹീനനായിരുന്ന കാലത്തു ഞാൻ നിന്നെ ശക്തനാക്കി; ഞാൻ നിന്നെ താങ്ങും.",
            englishText = "So do not fear, for I am with you; do not be dismayed, for I am your God. I will strengthen you and help you.",
            reference = "Isaiah 41:10",
            malayalamReference = "യെശയ്യാ 41:10",
            dayOfYear = 21
        ),
        BibleQuote(
            malayalamText = "ഞാൻ നിങ്ങളോടു സമാധാനം ശേഷിപ്പിച്ചുപോകുന്നു; എന്റെ സമാധാനം ഞാൻ നിങ്ങൾക്കു തരുന്നു.",
            englishText = "Peace I leave with you; my peace I give you. I do not give to you as the world gives.",
            reference = "John 14:27",
            malayalamReference = "യോഹന്നാൻ 14:27",
            dayOfYear = 22
        ),
        BibleQuote(
            malayalamText = "നിങ്ങളെ പരീക്ഷിച്ചവൻ വിശ്വസ്തൻ; അവൻ നിങ്ങൾക്കു വഹിപ്പാൻ കഴിയുന്നതിന്നുമീതെ പരീക്ഷ വരുവാൻ സമ്മതിക്കയില്ല.",
            englishText = "God is faithful; he will not let you be tempted beyond what you can bear.",
            reference = "1 Corinthians 10:13",
            malayalamReference = "1 കൊരിന്ത്യർ 10:13",
            dayOfYear = 23
        ),
        BibleQuote(
            malayalamText = "എന്റെ നുകം എളിമയുള്ളതും എന്റെ ചുമടു ഭാരം കുറഞ്ഞതും ആകുന്നു.",
            englishText = "For my yoke is easy and my burden is light.",
            reference = "Matthew 11:30",
            malayalamReference = "മത്തായി 11:30",
            dayOfYear = 24
        ),
        BibleQuote(
            malayalamText = "സകലവും ചെയ്‍വാൻ എനിക്കു ശക്തിയുണ്ടു ക്രിസ്തുവിൽ എന്നെ ശക്തനാക്കുന്നവനാൽ.",
            englishText = "I can do all this through him who gives me strength.",
            reference = "Philippians 4:13",
            malayalamReference = "ഫിലിപ്പിയർ 4:13",
            dayOfYear = 25
        ),
        // Add more quotes to reach 365...
        // For demo purposes, I'll add a few more and cycle through them
        BibleQuote(
            malayalamText = "ദൈവത്തിന്നു സകലവും സാദ്ധ്യം.",
            englishText = "With God all things are possible.",
            reference = "Matthew 19:26",
            malayalamReference = "മത്തായി 19:26",
            dayOfYear = 26
        ),
        BibleQuote(
            malayalamText = "നന്മയാൽ തിന്മയെ ജയിക്ക.",
            englishText = "Do not be overcome by evil, but overcome evil with good.",
            reference = "Romans 12:21",
            malayalamReference = "റോമർ 12:21",
            dayOfYear = 27
        ),
        BibleQuote(
            malayalamText = "നിന്റെ വഴിയെ യഹോവയ്ക്കു ഭരമേല്പിക്ക; അവനിൽ ആശ്രയിക്ക; അവൾ നിവൃത്തിക്കും.",
            englishText = "Commit your way to the LORD; trust in him and he will do this.",
            reference = "Psalm 37:5",
            malayalamReference = "സങ്കീർത്തനം 37:5",
            dayOfYear = 28
        ),
        BibleQuote(
            malayalamText = "യഹോവയുടെ നാമം ബലമുള്ള ദുർഗ്ഗം; നീതിമാൻ അതിലേക്കു ഓടിചെന്നു രക്ഷപ്രാപിക്കുന്നു.",
            englishText = "The name of the LORD is a fortified tower; the righteous run to it and are safe.",
            reference = "Proverbs 18:10",
            malayalamReference = "സദൃശവാക്യങ്ങൾ 18:10",
            dayOfYear = 29
        ),
        BibleQuote(
            malayalamText = "യഹോവ നല്ലവൻ; കഷ്ടകാലത്തു അവൻ ദുർഗ്ഗം; തന്നിൽ ആശ്രയിക്കുന്നവരെ അറിയുന്നു.",
            englishText = "The LORD is good, a refuge in times of trouble. He cares for those who trust in him.",
            reference = "Nahum 1:7",
            malayalamReference = "നഹൂം 1:7",
            dayOfYear = 30
        )
    )

    /**
     * Get quote for today based on day of year
     */
    fun getTodayQuote(): BibleQuote {
        val calendar = java.util.Calendar.getInstance()
        val dayOfYear = calendar.get(java.util.Calendar.DAY_OF_YEAR)

        // Cycle through quotes if we don't have 365
        val index = (dayOfYear - 1) % quotes.size
        return quotes[index]
    }

    /**
     * Get all quotes (for testing/preview)
     */
    fun getAllQuotes(): List<BibleQuote> = quotes
}