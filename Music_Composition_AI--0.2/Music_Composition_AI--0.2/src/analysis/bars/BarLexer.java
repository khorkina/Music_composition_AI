package analysis.bars;

import analysis.harmonic.ChordDegree;
import analysis.harmonic.ChordDegreeProcessor;
import analysis.harmonic.MetadataExtractor;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import tonality.Tonality;

import java.util.ArrayList;

public class BarLexer {
    private double barDuration_;
    private ArrayList<Bar> bars_;
    private double quantum_;
    private double r_;
    private Tonality tonality_;
    private int barNumber_;

    /**
     * Constructor. Lexes the score bar by bar and segments rythms.
     * @param score The score to lex
     * @param tonality The tonality of the score
     */
    public BarLexer(Score score, Tonality tonality) {
        barDuration_ = MetadataExtractor.computeBarUnit(score.getDenominator()) * score.getNumerator();
        bars_ = new ArrayList<>();
        tonality_ = tonality;
        barNumber_ = (int)((score.getEndTime() + barDuration_) / barDuration_);
        findQuantum(score);
        lexBarsFromScore(score);
        segmentRhythms();
    }

    /**
     * Processes the sequence of chord degrees in the lexed score
     * @return
     */
    public ArrayList<ChordDegree> getDegreeSequence() {
        ArrayList<ChordDegree> degrees = new ArrayList<>();
        ChordDegreeProcessor cdp = new ChordDegreeProcessor(tonality_);

        for (Bar bar : bars_) {
            ArrayList<Integer> pitches = new ArrayList<>();

            for (BarNote bn : bar.getNotes())
                pitches.add(bn.getPitch());

            degrees.add(cdp.chordToDegree(pitches, 1));
        }
        return degrees;
    }

    /**
     * Getter for the Bar list
     * @return The list of lexed bars
     */
    public ArrayList<Bar> getBars() {
        return bars_;
    }

    private void lexBarsFromScore(Score score) {
        for (int i = 0; i < barNumber_; ++i)
            bars_.add(i, new Bar());

        for (Part p : score.getPartArray()) {
            for (Phrase phrase : p.getPhraseArray()) {
                for (int i = 0; i < phrase.length(); ++i) {
                    Note note = phrase.getNote(i);
                    double time = phrase.getNoteStartTime(i);
                    double duration = normalizeDuration(note.getRhythmValue());
                    if (time % barDuration_ + duration > barDuration_) {
                        BarNote newHalfNote = new BarNote(0.0, (time + duration) % barDuration_, note.getPitch());
                        bars_.get((int)(time / barDuration_) + 1).addNote(newHalfNote);
                        duration -= (time + duration) % barDuration_;
                    }
                    BarNote newNote = new BarNote(time % barDuration_, duration, note.getPitch());
                    bars_.get((int)(time / barDuration_)).addNote(newNote);
                }
            }
        }
    }

    private void segmentRhythms() {
        for (Bar bar : bars_) {
            for (int i = 0; i < bar.getNotes().size(); ++i) {
                BarNote barNote = bar.getNotes().get(i);
                while (barNote.getDuration() > quantum_) {
                    double newNoteStartTime = barNote.getStartTime() + barNote.getDuration() - quantum_;
                    bar.getNotes().add(new BarNote(newNoteStartTime, quantum_, barNote.getPitch()));
                    barNote.setDuration(barNote.getDuration() - quantum_);
                }
            }
        }
    }

    private double normalizeDuration(double duration) {
        // truncate three decimals and convert to int.
        r_ += duration;
        int result = ((int)(r_ * 10000.0));
        // Rounded to the superior quantum_ multiple.
        int mod = ((int)(quantum_ * 10000.0));
        int rest = result % mod;
        if (rest != 0.0) {
            rest = rest > mod / 2 ? mod - rest : - rest;
            result = result + rest;
        }
        r_ =  (double) - rest / 10000.0;
        return (double) result / 10000.0;
    }

    private void findQuantum(Score score) {
        double srm = score.getShortestRhythmValue();
        if (srm < (0.0625 - 0.03125 / 2))
            quantum_ = 0.03125;
        else if (srm < (0.125 - 0.0625 / 2))
            quantum_ = 0.0625;
        else if (srm < (0.250 - 0.125 / 2))
            quantum_ =  0.125;
        else if (srm < (0.500 - 0.250 / 2))
            quantum_ = 0.250;
        else if (srm < (1 - 0.5 / 2))
            quantum_ =  0.500;
        else if (srm < (2.0 - 1.0 / 2.0))
            quantum_ = 1.00;
        else if (srm < (4.0 - 2.0 / 2.0))
            quantum_ = 2.00;
        else if (srm < (8.0 - 4.0 / 2.0))
            quantum_ = 4.00;
        else
            quantum_ = 8.00;
    }

    public int getBarNumber_() { return barNumber_; }
}
