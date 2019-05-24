public class Parser {
   private Token token;
   private Scanner scanner;

   public Parser(String input) {
      this.scanner = new Scanner(input);
      advance();
   }

   private void advance() {
      this.token = this.scanner.nextToken();
   }

   public void program() {

   }
}
