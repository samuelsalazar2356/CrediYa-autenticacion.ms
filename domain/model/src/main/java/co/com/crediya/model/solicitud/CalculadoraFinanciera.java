package co.com.crediya.model.solicitud;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Servicio de dominio para calculos financieros de amortizacion y cuotas.
 * Implementa la formula de anualidades para sistemas de amortizacion frances.
 */
public class CalculadoraFinanciera {

    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);

    public static BigDecimal calcularCuotaMensual(BigDecimal monto, BigDecimal tasaMensual, int plazoMeses) {
        if (monto == null || tasaMensual == null || plazoMeses <= 0 || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // Formula: Cuota = P * ( i * (1 + i)^n ) / ( (1 + i)^n - 1 )
        BigDecimal unoMasI = BigDecimal.ONE.add(tasaMensual);
        BigDecimal unoMasIPotenciaN = unoMasI.pow(plazoMeses, MC);

        BigDecimal numerador = monto.multiply(tasaMensual, MC).multiply(unoMasIPotenciaN, MC);
        BigDecimal denominador = unoMasIPotenciaN.subtract(BigDecimal.ONE, MC);

        if (denominador.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return numerador.divide(denominador, 2, RoundingMode.HALF_UP);
    }
}
