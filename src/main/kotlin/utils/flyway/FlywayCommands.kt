package utils.flyway

import org.flywaydb.core.Flyway

sealed interface FlywayCommand {
    fun run(flyway: Flyway)
}

object Migrate : FlywayCommand {
    override fun run(flyway: Flyway) {
        flyway.migrate()
    }
}

object Clean : FlywayCommand {
    override fun run(flyway: Flyway) {
        flyway.clean()
    }
}

object Info : FlywayCommand {
    override fun run(flyway: Flyway) {
        flyway.info()
    }
}

object Validate : FlywayCommand {
    override fun run(flyway: Flyway) {
        flyway.validate()
    }
}

object Baseline : FlywayCommand {
    override fun run(flyway: Flyway) {
        flyway.baseline()
    }
}

object Repair : FlywayCommand {
    override fun run(flyway: Flyway) {
        flyway.repair()
    }
}