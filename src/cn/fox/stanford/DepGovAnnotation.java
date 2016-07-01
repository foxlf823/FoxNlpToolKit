package cn.fox.stanford;



import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.util.ErasureUtils;

public class DepGovAnnotation implements CoreAnnotation<Integer> {
    public Class<Integer> getType() {
      return ErasureUtils.<Class<Integer>> uncheckedCast(Integer.class);
    }
  }