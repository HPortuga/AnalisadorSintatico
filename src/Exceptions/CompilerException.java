package Exceptions;

public class CompilerException extends RuntimeException
{
   private String msg;

   public CompilerException(){}

   public CompilerException(String str)
   {
      super(str);
      msg = str;
   }

   public String toString()
   {
      return msg;
   }
}
