package Exceptions;

import Nomes.Names;

public class BadVariableException extends CompilerException {
   public BadVariableException(Names expected, Names shown, int linha) {
      super("Classe mal formada; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
