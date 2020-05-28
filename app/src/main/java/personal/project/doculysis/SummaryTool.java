package personal.project.doculysis;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;


public class SummaryTool {
    FileInputStream in;
    FileOutputStream out;
    ArrayList<Sentence> sentences, contentSummary;
    ArrayList<Paragraph> paragraphs;
    int noOfSentences, noOfParagraphs;

    double[][] intersectionMatrix;
    LinkedHashMap<Sentence,Double> dictionary;


    SummaryTool(){
        in = null;
        out = null;
        noOfSentences = 0;
        noOfParagraphs = 0;
    }
    void init(){

        sentences = new ArrayList<Sentence>();
        paragraphs = new ArrayList<Paragraph>();
        contentSummary = new ArrayList<Sentence>();
        dictionary = new LinkedHashMap<Sentence,Double>();
        noOfSentences = 0;
        noOfParagraphs = 0;
      //  extractSentenceFromContext();

    }

    void extractSentenceFromContext(String path){
        int nextChar,j=0;
        int prevChar = -1;
        File file=new File(Environment.getExternalStorageDirectory(),path);
        String fileName=file.getName();

        try{
            BufferedReader in=new BufferedReader(new FileReader(file));
            while((nextChar = in.read()) != -1) {
                j=0;
                String temp="";
                while((char)nextChar != '.'){
                    //System.out.println(nextChar + " ");
                    temp+=(char)nextChar;
                    if((nextChar = in.read()) == -1){
                        break;
                    }
                    if((char)nextChar == '\n' && (char)prevChar == '\n'){
                        noOfParagraphs++;
                    }
                    j++;
                    prevChar = nextChar;
                }

                sentences.add(new Sentence(noOfSentences,(new String(temp)).trim(),(new String(temp)).trim().length(),noOfParagraphs));
                noOfSentences++;
                prevChar = nextChar;
            }
            in.close();
        //    groupSentencesIntoParagraphs();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    void groupSentencesIntoParagraphs(){
        int paraNum = 0;
        Paragraph paragraph = new Paragraph(0);

        for(int i=0;i<noOfSentences;i++){
            if(sentences.get(i).paragraphNumber == paraNum){
                //continue
            }else{
                paragraphs.add(paragraph);
                paraNum++;
                paragraph = new Paragraph(paraNum);

            }
            paragraph.sentences.add(sentences.get(i));
        }

        paragraphs.add(paragraph);
        //createIntersectionMatrix();
    }
    double noOfCommonWords(Sentence str1, Sentence str2){
        double commonCount = 0;

        for(String str1Word : str1.value.split("\\s+")){
            for(String str2Word : str2.value.split("\\s+")){
                if(str1Word.compareToIgnoreCase(str2Word) == 0){
                    commonCount++;
                }
            }
        }

        return commonCount;
    }

    void createIntersectionMatrix(){
        intersectionMatrix = new double[noOfSentences][noOfSentences];
        for(int i=0;i<noOfSentences;i++){
            for(int j=0;j<noOfSentences;j++){

                if(i<=j){
                    Sentence str1 = sentences.get(i);
                    Sentence str2 = sentences.get(j);
                    intersectionMatrix[i][j] = noOfCommonWords(str1,str2) / ((double)(str1.noOfWords + str2.noOfWords) /2);
                }else{
                    intersectionMatrix[i][j] = intersectionMatrix[j][i];
                }

            }
        }
       // createDictionary();
    }

    void createDictionary(){
        for(int i=0;i<noOfSentences;i++){
            double score = 0;
            for(int j=0;j<noOfSentences;j++){
                score+=intersectionMatrix[i][j];
            }
            dictionary.put(sentences.get(i), score);
            ((Sentence)sentences.get(i)).score = score;
        }
        //createSummary();
    }

    void createSummary(){

        for(int j=0;j<=noOfParagraphs;j++){
            int primary_set = paragraphs.get(j).sentences.size()/5;

            //Sort based on score (importance)
            Collections.sort(paragraphs.get(j).sentences,new SentenceComparator());
            for(int i=0;i<=primary_set;i++){
                contentSummary.add(paragraphs.get(j).sentences.get(i));
            }
        }

        //To ensure proper ordering
        Collections.sort(contentSummary,new SentenceComparatorForSummary());
        printSummary();
    }
    public String printSummary(){
//		System.out.println("no of paragraphs = "+ noOfParagraphs);
        String ans="";
        for(Sentence sentence : contentSummary){
            //tv_output.setText(sentence.value);
            ans+=sentence.value;
        }
        return ans;
    }


   /* private String readText(String  input)
    {
        File file=new File(Environment.getExternalStorageDirectory(),input);
        StringBuilder text=new StringBuilder();
        try{
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line;
            while((line=br.readLine())!=null)
            {
                text.append(line);
                text.append("\n");

            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return text.toString();
    }

    double getWordCount(ArrayList<Sentence> sentenceList){
        double wordCount = 0.0;
        for(Sentence sentence:sentenceList){
            wordCount +=(sentence.value.split(" ")).length;
        }
        return wordCount;
    }

//
//    //select file  from storage
//    private void performFileSearch()
//    {
//        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("text/*");
//        startActivityForResult(intent,READ_REQUEST_CODE);
//    }*/
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        if(requestCode==READ_REQUEST_CODE && resultCode== Activity.RESULT_OK)
//        {
//            if(data!=null)
//            {
//                Uri uri =data.getData();
//                path=uri.getPath();
//                path=path.substring(path.indexOf(":")+1);
//                // if(path.contains("emulated")){
//                //    path=path.substring(path.indexOf("0")+1);
//                // }
//                Toast.makeText(this,""+path,Toast.LENGTH_SHORT).show();
//                // tv_output.setText(readText(path));
//            }
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode== PERMISSION_REQUEST_STORAGE)
//        {
//            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
//            {
//                Toast.makeText(this,"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                Toast.makeText(this,"PERMISSION NOT GRANTED",Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//
//    }
}




