package Exceptions;

import Nomes.Names;

public class BadForException extends CompilerException {
   public BadForException(Names expected, Names shown, int linha) {
      super("For mal formado; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
