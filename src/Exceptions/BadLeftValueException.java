package Exceptions;

import Nomes.Names;

public class BadLeftValueException extends CompilerException {
   public BadLeftValueException(Names expected, Names shown, int linha) {
      super("Classe mal formada; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
