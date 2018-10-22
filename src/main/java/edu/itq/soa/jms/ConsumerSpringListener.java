package edu.itq.soa.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.xmlbeans.XmlException;

import com.soa.amortizacion.AmortizacionStub;
import com.soa.amortizacion.Registro_type0;
import com.soa.amortizacion.RequestAmortizacion;
import com.soa.amortizacion.ResponseAmortizacion;
import com.soa.amortizacion.Tabla_type0;

import edu.itq.soa.buro.ApellidoMaterno_type1;
import edu.itq.soa.buro.ApellidoPaterno_type1;
import edu.itq.soa.buro.CreditResponse;
import edu.itq.soa.buro.CreditServiceStub;
import edu.itq.soa.buro.Curp_type1;
import edu.itq.soa.buro.Nombres_type1;
import edu.itq.soa.credit.ClientData;
import edu.itq.soa.credit.CreditRequestDocument;
import edu.itq.soa.credit.CreditResponseDocument;
import edu.itq.soa.credit.TablaAmortizacion;
import edu.itq.soa.credit.TablaAmortizacion.Tabla;
import edu.itq.soa.credit.TablaAmortizacion.Tabla.Registro;

public class ConsumerSpringListener implements MessageListener {

    JmsSender jmsSender;

    // CreditServiceStub stubBuro;
    @Override
    public void onMessage(Message message) {
        // TODO Auto-generated method stub

        try {
            final String msg = ((TextMessage) message).getText();
            // Recuperar xml de la cola
            CreditRequestDocument doc = CreditRequestDocument.Factory
                    .parse(msg);
            // objeto que nos ayudara a acceder a los datos del XML de la cola
            ClientData req = doc.getCreditRequest();
            // Obtener datos de registro del xml recuperado de la cola.
            String nombresQueue = req.getNombres();
            String apellidoPaternoQueue = req.getApellidoPaterno();
            String apellidoMaternoQueue = req.getApellidoMaterno();
            String curpQueue = req.getCurp();
            String domicilioQueue = req.getDomicilio();
            double montoQueue = req.getMonto();
            int plazoQueue = req.getPlazo();
            double tasaQueue = req.getTasaInteres();
            // Conectar al servicio de BURO
            CreditServiceStub stubBuro = new CreditServiceStub(
                    "http://localhost:8089/axis2/services/CreditService?wsdl");
            // Crear elemento request para llenar los datos del registro
            edu.itq.soa.buro.CreditRequest serviceReq = new edu.itq.soa.buro.CreditRequest();
            // Nombre
            Nombres_type1 nombre = new Nombres_type1();
            // agregar elemento "nombres" de la cola, al objeto Nombres_Type1
            nombre.setNombres_type0(nombresQueue);
            // agregar objeto Nombres_Type1 al request del servicio Buro
            serviceReq.setNombres(nombre);
            // Apellido Paterno
            ApellidoPaterno_type1 apellidoPaterno = new ApellidoPaterno_type1();
            apellidoPaterno.setApellidoPaterno_type0(apellidoPaternoQueue);
            serviceReq.setApellidoPaterno(apellidoPaterno);
            // Apellido Materno
            ApellidoMaterno_type1 apellidoMaterno = new ApellidoMaterno_type1();
            apellidoMaterno.setApellidoMaterno_type0(apellidoMaternoQueue);
            serviceReq.setApellidoMaterno(apellidoMaterno);
            // curp
            Curp_type1 curp = new Curp_type1();
            curp.setCurp_type0(curpQueue);
            serviceReq.setCurp(curp);
            // Domicilio
            serviceReq.setDomicilio(domicilioQueue);
            // Enviar request a servicio Buro
            CreditResponse buroResponse = stubBuro.creditOperation(serviceReq);
            // obtener respuesta del servicio Buro
            boolean resBuro = buroResponse.getAutorizado();
            System.out.println("Respuesta de Buro de credito: " + resBuro);

            if (resBuro) {
                CreditResponseDocument respDoc = CreditResponseDocument.Factory
                        .newInstance();
                edu.itq.soa.credit.CreditResponseDocument.CreditResponse creditResponse = respDoc
                        .addNewCreditResponse();
                ClientData clientData = creditResponse.addNewClient();
                clientData.setNombres(req.getNombres());
                clientData.setApellidoPaterno(req.getApellidoPaterno());
                clientData.setApellidoMaterno(req.getApellidoMaterno());
                clientData.setCurp(req.getCurp());
                clientData.setDomicilio(req.getDomicilio());
                clientData.setCorreo(req.getCorreo());
                clientData.setSalario(req.getSalario());
                clientData.setMonto(req.getMonto());
                clientData.setPlazo(req.getPlazo());
                clientData.setTasaInteres(req.getTasaInteres());
                jmsSender.sendMessage("queue/A", respDoc.toString());
            } else {
                // Llamar a servivio Amortizacion
                AmortizacionStub stubAmortizacion = new AmortizacionStub(
                        "http://localhost:8089/axis2/services/amortizacion?wsdl");
                // Crear objeto de request para el servicio de amortizacion
                RequestAmortizacion amortizacionReq = new RequestAmortizacion();
                // Agregar datos extraidos de la cola al request
                amortizacionReq.setInteresAnual(tasaQueue);
                amortizacionReq.setMontoTotal(montoQueue);
                amortizacionReq.setNumeroPagos(plazoQueue);
                ResponseAmortizacion responseAmortizacion = stubAmortizacion
                        .amortizacion(amortizacionReq);
                Tabla_type0 tablaAmortizacion = responseAmortizacion.getTabla();
                Registro_type0 filasTabla[] = tablaAmortizacion.getRegistro();
                CreditResponseDocument respDoc = CreditResponseDocument.Factory
                        .newInstance();
                edu.itq.soa.credit.CreditResponseDocument.CreditResponse creditResponse = respDoc
                        .addNewCreditResponse();
                ClientData clientData = creditResponse.addNewClient();
                clientData.setNombres(req.getNombres());
                clientData.setApellidoPaterno(req.getApellidoPaterno());
                clientData.setApellidoMaterno(req.getApellidoMaterno());
                clientData.setCurp(req.getCurp());
                clientData.setDomicilio(req.getDomicilio());
                clientData.setCorreo(req.getCorreo());
                clientData.setSalario(req.getSalario());
                clientData.setMonto(req.getMonto());
                clientData.setPlazo(req.getPlazo());
                clientData.setTasaInteres(req.getTasaInteres());
                TablaAmortizacion amortizacion = creditResponse.addNewTabla();
                Tabla tabla = Tabla.Factory.newInstance();
                System.out.println(
                        "Periodo\tSaldo Inicial\tCuotas\tIntereses\tAbono Capital\tSaldo Final");
                for (Registro_type0 registro_type0 : filasTabla) {
                    System.out.println("\t" + registro_type0.getPeriodo() + "\t"
                            + registro_type0.getSaldoInicial() + "\t"
                            + registro_type0.getCuotas() + "\t"
                            + registro_type0.getIntereses() + "\t"
                            + registro_type0.getAbonoCapital() + "\t"
                            + registro_type0.getSaldoFinal() + "\t");
                    Registro registro = tabla.addNewRegistro();
                    registro.setPeriodo(registro_type0.getPeriodo());
                    registro.setSaldoInicial(registro_type0.getSaldoInicial());
                    registro.setCuotas(registro_type0.getCuotas());
                    registro.setIntereses(registro_type0.getIntereses());
                    registro.setAbonoCapital(registro_type0.getAbonoCapital());
                    registro.setSaldoFinal(registro_type0.getSaldoFinal());
                }
                amortizacion.setTabla(tabla);
                jmsSender.sendMessage("queue/A", respDoc.toString());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param jmsSender the jmsSender to set
     */
    public void setJmsSender(JmsSender jmsSender) {
        this.jmsSender = jmsSender;
    }

}
