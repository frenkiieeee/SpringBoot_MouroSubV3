package com.mourosub.web.exception;

public class exceptions {
    //Excepcion de reservas
    public static class ReservaNotFoundException extends RuntimeException{
        public ReservaNotFoundException (String message){
            super(message);
        }
    }
    //Exception de cancelacion de reserva
    public static class ReservaYaCanceladaException extends RuntimeException{
        public ReservaYaCanceladaException(String message){
        super(message);
        }
    }
    //Exception por cancelacion por fuera de plazo
    public static class FueraDePlazoException extends RuntimeException{
        public FueraDePlazoException(String message){
            super(message);
        }
    }
    //Excepcion por Actividad no encontrada
    public static class ActividadNotFoundException extends RuntimeException {
        public ActividadNotFoundException (String message){
            super(message);
        }
    }
    //Excepcion para el dni
    public static class DniNotFoundException extends RuntimeException{
        public DniNotFoundException (String message){
            super(message);
        }
    }
    //Excepcion por instructor no encontrado
    public static class InstructorNotFoundException extends RuntimeException {
        public InstructorNotFoundException (String messsage){
            super(messsage);
        }
    }
    //Excepcion nombre obligatorio
    public static class NombreNotFoundException extends RuntimeException {
        public NombreNotFoundException (String messsage){
            super(messsage);
        }
    }
    //Excepcion por usuario no encontrado
    public static class UsuarioNotFoundException extends RuntimeException{
        public UsuarioNotFoundException (String message){
        super(message);
        }
    }
    //Excepcion por que no hay plazas disponibles
    public static class SinPlazasDisponiblesException extends RuntimeException{
        public SinPlazasDisponiblesException (String message){
            super(message);
        }
    }

    public static class MaterialNotFoundException extends RuntimeException{
        public MaterialNotFoundException (String message){
            super(message);
        }
    }

    public static class MaterialSinExistenciasException extends RuntimeException{
        public MaterialSinExistenciasException (String message){
            super(message);
        }
    }

    public static class SeguroNotFoundException extends RuntimeException {
        public SeguroNotFoundException (String message){
        super(message);
        }
    }
    public static class CompaniaSeguroNotFoundException extends RuntimeException {
        public CompaniaSeguroNotFoundException (String message){
            super(message);
        }
    }

    public static class AlquilerNotFoundException extends RuntimeException {
        public AlquilerNotFoundException (String message){
            super(message);
        }
    }
    public static class SinAlquilerDisponibleException extends RuntimeException{
        public SinAlquilerDisponibleException (String message){
            super (message);
        }
    }
}
