package ntd.ntu.btl_64130297_quizz;

public class Question {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
    private String image_MinhHoa;
    private int image_MinhHoaId;

    public Question() {
    }
    public Question(String question, String optionA, String optionB, String optionC, String optionD, String answer, String image_MinhHoa, int image_MinhHoaId) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.image_MinhHoa = image_MinhHoa;
        this.image_MinhHoaId = image_MinhHoaId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImage_MinhHoa() {
        return image_MinhHoa;
    }

    public void setImage_MinhHoa(String image_MinhHoa) {
        this.image_MinhHoa = image_MinhHoa;
    }

    public int getImage_MinhHoaId() {
        return image_MinhHoaId;
    }

    public void setImage_MinhHoaId(int image_MinhHoaId) {
        this.image_MinhHoaId = image_MinhHoaId;
    }
}
