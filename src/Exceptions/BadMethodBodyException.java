package Exceptions;

import Nomes.Names;

public class BadMethodBodyException extends CompilerException {
   public BadMethodBodyException(Names expected, Names shown, int linha) {
      super("Corpo de metodo mal formado; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
