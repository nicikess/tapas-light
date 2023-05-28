package ch.unisg.tapasexecutorpool;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DependencyRuleTests {

	@Test
	void testPackageDependencies() {
		noClasses()
				.that()
				.resideInAPackage("ch.unisg.tapasexecutorpool.pool.domain..")
				.should()
				.dependOnClassesThat()
				.resideInAnyPackage("ch.unisg.tapasexecutorpool.pool.application..")
				.check(new ClassFileImporter()
						.importPackages("ch.unisg.tapasexecutorpool.pool.."));
	}
}
