package Exceptions;

import Nomes.Names;

public class BadParamException extends CompilerException {
   public BadParamException(Names expected, Names shown, int linha) {
      super("Parametro mal formado; Esperado: " + (expected == null? "int || string || ID" : expected)
            + ", apareceu: " + shown + " na linha " + linha);
   }
}
