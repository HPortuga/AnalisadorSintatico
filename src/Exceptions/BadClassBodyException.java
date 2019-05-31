package Exceptions;

import Nomes.Names;

public class BadClassBodyException extends CompilerException {
   public BadClassBodyException(Names expected, Names shown, int linha) {
      super("Corpo do metodo mal formado; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
