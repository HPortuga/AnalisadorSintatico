package Exceptions;

import Nomes.Names;

public class BadClassException extends CompilerException {

   public BadClassException(Names expected, Names shown, int linha) {
      super("Classe mal formada; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }

}
