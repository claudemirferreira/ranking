package br.com.setebit.ranking.infrastructure.report;

import br.com.setebit.ranking.application.dto.RankingPdfRow;
import br.com.setebit.ranking.application.port.out.RankingPdfReportPort;
import br.com.setebit.ranking.domain.exception.BusinessRuleException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class JasperRankingPdfReportAdapter implements RankingPdfReportPort {

	private static final String REPORT_PATH = "reports/ranking.jrxml";
	private static final String LOGO_PATH = "reports/images/logo-adventista.png";

	private volatile JasperReport compiledReport;

	@Override
	public byte[] generate(Map<String, Object> parameters, List<RankingPdfRow> rows) {
		try {
			JasperReport report = compiledReport();
			Map<String, Object> params = parameters == null ? new HashMap<>() : new HashMap<>(parameters);

			ClassPathResource logoResource = new ClassPathResource(LOGO_PATH);
			try (InputStream logoStream = logoResource.getInputStream()) {
				params.put("LOGO", logoStream);
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);
				JasperPrint print = JasperFillManager.fillReport(report, params, dataSource);
				return JasperExportManager.exportReportToPdf(print);
			}
		}
		catch (Exception ex) {
			throw new BusinessRuleException("Falha ao gerar PDF do ranking: " + ex.getMessage());
		}
	}

	private JasperReport compiledReport() throws Exception {
		JasperReport local = compiledReport;
		if (local != null) {
			return local;
		}
		synchronized (this) {
			if (compiledReport == null) {
				ClassPathResource resource = new ClassPathResource(REPORT_PATH);
				try (InputStream input = resource.getInputStream()) {
					compiledReport = JasperCompileManager.compileReport(input);
				}
			}
			return compiledReport;
		}
	}
}
