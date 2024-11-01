package analysis.harmonic;

import analysis.bars.Bar;
import analysis.bars.BarLexer;

import java.util.ArrayList;
import java.util.Arrays;

public class ChordDegreeExtractor {
    private BarLexer barLexer_;
    private ArrayList<ChordDegree> degreeList_;

    public ChordDegreeExtractor(BarLexer barLexer) {
        barLexer_ = barLexer;
    }

    /**
     * Process the sequence of chord degrees in the lexed score
     * @return the degree sequence of the score
     */
    public ArrayList<ChordDegree> getDegreeSequence() {
        preprocessDegreeExtraction();

        ArrayList<ChordDegree> degrees = new ArrayList<>();
        ChordDegreeProcessor cdp = new ChordDegreeProcessor(barLexer_.getTonality());

        for (Bar bar : barLexer_.getBars())
            degrees.addAll(getDegreesInSubBar(bar, barLexer_.getBeatsPerBar(), 1, 0, barLexer_.getBeatsPerBar(), cdp));

        return degrees;
    }

    /**
     * Segment rhythms in quantum-long notes and group notes by beat unit
     */
    private void preprocessDegreeExtraction() {
        for (Bar bar : barLexer_.getBars()) {
            bar.segmentRhythms(barLexer_.getQuantum());
            bar.groupNotesByBeat(barLexer_.getBeatsPerBar(), barLexer_.getBarUnit());
        }
    }

    private ArrayList<ChordDegree> getDegreesInSubBar(Bar bar, int num, int divider, int start, int end,
                                                      ChordDegreeProcessor cdp)
    {
        // Concatenate all notes to use in degree detection
        ArrayList<Integer> notes = new ArrayList<>();
        for (int i = start; i < end; ++i) {
            notes.addAll(bar.getNotesByBeat().get(i));
        }

        // Try to find degree
        ChordDegree cd = cdp.chordToDegree(notes, divider);

        // Return degree if found or if we cannot divide more
        if (cd.getDegree() > 0 || num / divider <= 1)
            return new ArrayList<>(Arrays.asList(cd));

        // Find the smallest divider of num / divider
        int sd;
        for (sd = 2; sd <= num; ++sd)
            if ((num / divider) % sd == 0)
                break;

        ArrayList<ChordDegree> degrees = new ArrayList<>();

        // Call recursively on sub-bars
        for (int i = 0; i < sd; ++i)
            degrees.addAll(getDegreesInSubBar(bar, num, divider * sd, start + i * ((end - start) / sd),
                    start + (i + 1) * ((end - start) / sd) - 1, cdp));

        return degrees;
    }
}
