package analysis.harmonic;

public class ChordDegree {
    private int degree_;
    private boolean seventhChord_;
    private int barFractionDen_;

    /**
     * Constructor for the ChordDegree class
     * @param degree Degree of the chord
     * @param seventhChord Specify whether it is a seventh chord or not
     */
    public ChordDegree(int degree, boolean seventhChord, int barFractionDen) {
        this.degree_ = degree;
        this.seventhChord_ = seventhChord;
        this.barFractionDen_ = barFractionDen;
    }

    /**
     * Getter for the degree attribute
     * @return the degree of the chord
     */
    public int getDegree() {
        return degree_;
    }

    /**
     * Getter for the seventhChord attribute
     * @return true if the chord is a seventh chord, false elsewhere
     */
    public boolean isSeventhChord() {
        return seventhChord_;
    }

    /**
     * Getter for the barFractionDen attribute
     * @return the denominator of the fraction of bar corresponding to this degree
     */
    public int getBarFractionDen_() {
        return barFractionDen_;
    }

    /**
     * Equality method
     * @param other other ChordDegree
     * @return true if the two ChordDegrees are equal, false elsewhere and if the param is not a ChordDegree
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof ChordDegree))
            return false;
        return degree_ == ((ChordDegree) other).degree_ && seventhChord_== ((ChordDegree) other).seventhChord_;
    }

    /**
     * Conversion to String
     * @return a String corresponding to the object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        switch (degree_) {
            case 0:
                sb.append("Ø");
                break;
            case 1:
                sb.append('I');
                break;
            case 2:
                sb.append("II");
                break;
            case 3:
                sb.append("III");
                break;
            case 4:
                sb.append("IV");
                break;
            case 5:
                sb.append('V');
                break;
            case 6:
                sb.append("VI");
                break;
            case 7:
                sb.append("VII");
                break;
        }
        if (seventhChord_)
            sb.append("7");

        sb.append("-1/").append(barFractionDen_).append(")");

        return sb.toString();
    }
}
