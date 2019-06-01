package Exceptions;

import Nomes.Names;

public class BadIfException extends CompilerException {
   public BadIfException(Names expected, Names shown, int linha) {
      super("If mal formado; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
