package Exceptions;

import Nomes.Names;

public class BadConstructorException extends CompilerException {
   public BadConstructorException(Names expected, Names shown, int linha) {
      super("Construtor mal formado; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
