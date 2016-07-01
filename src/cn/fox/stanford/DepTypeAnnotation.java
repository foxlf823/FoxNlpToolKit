package cn.fox.stanford;



import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.util.ErasureUtils;

public class DepTypeAnnotation implements CoreAnnotation<String>{
    public Class<String> getType() {
        return ErasureUtils.<Class<String>> uncheckedCast(String.class);
      }
}
