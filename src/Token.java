public class Token {
   private Names name;
   private String lexema;

   public Token(Names name) {
      this.name = name;
   }

   public Token(Names name, String lexema) {
      this.name = name;
      this.lexema = lexema;
   }

   public Names getName() {
      return name;
   }

   public void setName(Names name) {
      this.name = name;
   }

   public String getLexema() {
      return lexema;
   }

   public void setLexema(String lexema) {
      this.lexema = lexema;
   }

   @Override
   public String toString() {
      return "<"+name+","+lexema+">";
   }
}
