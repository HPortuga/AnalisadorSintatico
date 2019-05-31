package Exceptions;

import Nomes.Names;

public class BadSuperStatException extends CompilerException {
   public BadSuperStatException(Names expected, Names shown, int linha) {
      super("Classe mal formada; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
