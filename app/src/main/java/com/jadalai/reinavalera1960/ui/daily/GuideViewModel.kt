package com.jadalai.reinavalera1960.ui.daily

import androidx.lifecycle.ViewModel
import com.jadalai.reinavalera1960.data.local.dao.VerseWithDetails
import com.jadalai.reinavalera1960.data.repository.BibleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(
    private val repository: BibleRepository
) : ViewModel() {

    fun getVersesForRange(
        bookId: Int,
        chapterNumber: Int,
        startVerse: Int,
        endVerse: Int,
        translation: String
    ): Flow<List<VerseWithDetails>> {
        return repository.getVersesWithDetailsForRange(
            bookId = bookId,
            chapterNumber = chapterNumber,
            startVerse = startVerse,
            endVerse = endVerse,
            translation = translation
        )
    }
}
