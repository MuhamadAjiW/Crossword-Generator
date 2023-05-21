public class Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        CrosswordGenerator cg = new CrosswordGenerator("dictionaryShortestest.json");

        try{
            cg.initialize();
            cg.generate();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Generation failed");
            return;
        }

        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Total execution time in millis: " + elapsedTime/1000000);

        if(cg.crossword.maxWidth - cg.crossword.minWidth > 200){
            System.out.println("Width: " + (cg.crossword.maxWidth - cg.crossword.minWidth));
            System.out.println("Height: " + (cg.crossword.maxHeight - cg.crossword.minHeight));
            System.out.println("Crossword is too big to print");
        }
        else{
            System.out.println("Width: " + (cg.crossword.maxWidth - cg.crossword.minWidth));
            System.out.println("Height: " + (cg.crossword.maxHeight - cg.crossword.minHeight));
            System.out.println("Density: " + cg.crossword.matrix.size() + "/" + ((cg.crossword.maxWidth - cg.crossword.minWidth) * (cg.crossword.maxHeight - cg.crossword.minHeight)));
            System.out.println("Score: " + (Double.parseDouble(((Integer) cg.crossword.matrix.size()).toString())/Double.parseDouble(((Integer)((cg.crossword.maxWidth - cg.crossword.minWidth) * (cg.crossword.maxHeight - cg.crossword.minHeight))).toString())));
            System.out.println("result: \n");
            cg.crossword.print();
        }
    }
}
