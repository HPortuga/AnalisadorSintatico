package Exceptions;

import Nomes.Names;

public class BadProgramException extends CompilerException {
   public BadProgramException(Names expected, Names shown, int linha) {
      super("Programa mal formado; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
