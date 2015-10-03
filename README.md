<h1>Language Suite</h1>
Michael Hergenrader

<h2>Summary</h2>

Language Suite is a desktop application written in Java and the Swing GUI library that promotes practice in learning foreign languages. It includes two main modules: a notecard review module and a conjugation practice module. These modules allow different types of review: untimed practice and graded timed "test" modes as well as the ability for the user to create their own notecards and conjugated verbs for review.

The original motivations for this application were two-fold:
- A personal desire to continually practice vocabulary and conjugations in various languages. Sites like conjuguemos.com existed but had limited sets of verbs (and very few notecards) that were not customizable or able to be added on to and costed non-trivial fees for larger use by schools.
- The ability to practice anywhere without an internet connection. While a web application was also considered to be the implementation, the full offline capability of a desktop application at the time meant that server requests would not be needed during practice or saving. Perhaps future implementations could be on the web, taking advantage of the improved offline frameworks.

<h2>Notecard Module</h2>

A crucial piece of any foreign language learning is understanding vocabulary, both at the word and phrase levels. To this end, the notecard module allows for digital practice of word/phrase review concepts, either between languages or for words and their definitions/usages in a single language.

What is unique about this application is that it provides functionality of both "2-D" (two-sided) and "3-D" (three-sided) notecards, the latter of which is less accessible for easy vocabulary learning on paper. The two-sided notecards are standard, but three-sided cards allow for multiple different pieces to be reviewed. For example, learning target languages with glyphs, radicals, or characters that do not have directly readable phonetic mappings (like Chinese) necessitates learning the pronunciation, character, and source language meaning. Continuing the Chinese example, starting from English, one side could represent the pinyin (Anglicized pronunciation of the character or phrase), the character or phrase itself, and the English meaning(s). And during notecard review, in addition to the simple "flipping" interaction of two-sided notecards, three-sided notecards can be "rotated" across the three sides. This allows users to "flip" to just the sides of importance to them at the time (or in the case of textual entry, they can just provide their answer against one of the hidden sides). For example, if a user was less interested in the English meaning(s) and just wanted to practice the pronunciation and character representation, they could view or test against only those two sides. Thus, the three-sided notecard is a convenient and rich superset of the classic two-sided notecard. Each deck allows for any mix of two-sided and three-sided notecards, allowing words or phrases that require more information to expand to the third side, while keeping the standard format for more straightforward concepts.

Notecards above three sides have not been determined to have much more benefit for their increased complexity (by me, anyway, at least as of yet). This would depart from the familiar "flipping" interaction both of 2-D and 3-D notecards, forcing the user to specify which category (side) they would like from a larger list.

In the future, I plan to implement printing functionality (either to PDF or on paper) for offline review of both two-sided and three-sided notecards, perhaps to supplement foreign language learning textbooks. In the case of three-sided notecards, there are two possibilities for paper format.
- The first is to print one side on each uniform horizontal third of a notecard. The user would then fold these notecards between the thirds, ending up with a "3-D" paper notecard. Review would be rotating the card in the desired direction (in the style of a rolodex). While convenient for review, each notecard then requires more space, making portability difficult, especially as the number of notecards grows.
- The second is to print "front and back." In this case, the notecard would be folded at the quarter marks at the horizontal top and bottom. The front side (now at half the height) would contain the first side of the notecard. The back side (inner half under the folds) would have sides two and three printed at the top and bottom, respectively. The folds would then cover up sides two and three. This approach is more portable because notecards are still "2-D"/relatively flat, and review is still trivially implemented. If a user starts with side 1, then (s)he simply lifts the corresponding flap(s) for the other sides after turning over the notecard. On the other hand, when starting with side 2 or 3, the starting side has its fold lifted and then for reviewing the other side (2 or 3), one simply lifts the other flap. For reviewing side 1, the card is simply turned over.

<h2>Conjugated Verbs Module</h2>

An important piece of learning morphologically rich languages, the most common being Romance Languages, is understanding the various verb inflections for different subject pronouns. The conjugated verbs module, during untimed or timed review modes, will cycle through a "deck" of different verbs and their possible conjugations. The user is presented with the verb infinitive (the base form of the verb, not inflected) and a subject pronoun, from which the user must enter the correct inflection (or conjugation). When the user submits his/her answer, the application will announce whether the user is correct or not before moving on to the next verb-pronoun pair.

Before practice, a user may view the possible choices of infinitives, subject pronouns, and verb conjugation decks to select subsets or mix and match as they so choose. This allows for more focused practice on specific verbs (often irregulars) or a more diverse review when selecting multiple review decks. This latter review is recommended as a more final stage to demonstrate knowledge across a broad variety of conjugated verbs, perhaps even from different languages or dialects, making it closer to "real world" usage.

In the future, I plan to implement printing functionality (either to PDF or on paper) for offline review of conjugated verbs, perhaps to supplement foreign language learning textbooks.

<h2>Reports</h2>

At the end of each review session in either module, a report is generated and saved, presenting the user's score in the review of the decks and the cases that they missed. In the future, I plan to add functionality for emailing and printing the reports. These reports can be viewed later at any time.

<h2>Editors</h2>

Both the notecard and conjugated verb modules have the ability for users to add decks for their own practice of nearly any language.

At the highest level, users can add new language objects to contain such notecard or conjugated verb practice decks. For conjugated verb practice, users can add custom subjects or pronouns that will appear in verb practice against their provided infinitives.

In the notecard editor, users can create both two-sided and three-sided notecards for practice, supplying either their own titles and metadata for the decks and cards or defaulting to the first side of the card. Users may choose to rotate the card deck sides or individual cards for more detailed customization. For example, if the user desires one side to be the designated "front," this can easily be changed on a per-card or per-deck level.

In the conjugated verb editor, users specify infinitives and the proper inflections for either the entire set of subject pronouns or some subset. The latter case is useful if the language created has too broad a set of pronouns for this review deck (only non-empty inflections will be tested in review mode). One specific example is the regional use of the pronoun vosotros in Spanish. In parts of Spain, this refers to the concept of "you all," whereas in most Latin American Spanish dialects, the ustedes form serves a purpose of both "they" and "you all," making this subject pronoun irrelevant.

<h2>Other Functionality</h2>

<h3>Auto-accenting last character</h3>

For users that do not know the shortcuts required to add diacritical marks to certain characters (like acute and grave accents, circumflexes, diereses, etc.), the review and editor modules, when accepting textual inputs, contain a button that quickly adds an accent to the last character. This functionality currently covers most accents for Northern and Western European languages, allowing vowel accents (of grave, acute, dieresis, and tilde varieties), tildes, and circumflexes, also covering the special "ç" case in languages like Portuguese.

When typing an input, for a character that a user would like to accent, (s)he simply presses the button to cycle through the accents applied to the final character.

Future functionality would be to add a "world keyboard," containing an even more accessible panel of more characters that the user could enter, expanding the possible language set usable by this functionality as well.

<h3>Storage of all deck data</h3>

The decks for both notecards and conjugated verbs are stored in serialized files in their particular languages. These files are easily distributable, allowing for different users to contribute, modify, and share different decks in an easily usable format.

<h2>Settings</h2>

There are several options during review of conjugations or notecards:
- When a user inputs an incorrect answer, whether or not the program should provide instant feedback on the correct answer.
- When a user inputs a correct answer, whether or not the program should remain on the current notecard or infinitive/pronoun pair until the user provides the correct answer.
- Whether the clock should be paused at each feedback presentation.
- Whether the deck should be shuffled after each pass through all notecards or infinitive/pronoun pairs.
- Whether the incorrect answer should be cleared after the feedback dialog is dismissed (allowing users to edit it and see what they missed specifically)
- Whether the user wants to run through the deck once or whether it should be repeated during the time window
- Practice or Test modes: the latter is timed and clears answers, while practice mode is untimed and takes pauses at the clock

<h2>Application, Source Code Licensing</h2>

This application follows the MIT License - see LICENSE for more information.

<h2>Contact Information</h2>

All inquiries can be referred to mike.hergenrader@gmail.com.