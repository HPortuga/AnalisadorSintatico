package Exceptions;

import Nomes.Names;

public class BadMethodException extends CompilerException {
   public BadMethodException(Names expected, Names shown, int linha) {
      super("Metodo mal formado; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
