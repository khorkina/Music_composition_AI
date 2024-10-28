import tools.Misc;

import java.util.Random;

/**
 * This class contains static public variables that
 * specify runtime behavior.
 * Initialization is the default value
 */
public class ExecutionParameters {
    /**
     * Specify if Verbose mode is activated
     */
    public static boolean verboseMode = false;

    /**
     * Specify if we create the training set (analyze .mid files and create input files for training)
     */
    public static boolean analyze = false;

    /**
     * Specify if we train the program (if we fill and save the Markov chains)
     */
    public static boolean train = false;

    /**
     * Specify if we generate a song
     */
    public static boolean generate = false;

    /**
     * Specify if we generate a WAV
     */
    public static boolean generateWav = false;

    /**
     * Specify the seed for generation
     */
    public static String seed = "lyreland" + new Random().nextInt(800000);

    /**
     * Specify the path to input mid Files
    */
    public static String midDirPath = Misc.getJarPath() + "../assets/midi/";

    /**
     * Specify the path to the training set (for writing or reading)
     */
    public static String trainingSetPath = Misc.getJarPath() + "training/training-set";

    /**
     * Specify the path to the trained data (created after training and used for generation)
     */
    public static String trainedDataPath = Misc.getJarPath() + "training/trained-data";

    /**
     * Specify the path to the generated files without the extension. Default value is `generated.mid` and `generated.wav`
     */
    public static String outputPath = Misc.getJarPath() + "../generated";


}
