package io.github.jsixface.client.pages

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import io.github.jsixface.client.viewModels.JobsViewModel
import io.github.jsixface.common.JobStatus.InProgress
import io.github.jsixface.common.JobStatus.Queued
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text

@Composable
fun ShowJobList(viewModel: JobsViewModel) {
    val jobList by viewModel.jobList.collectAsState(emptyList())
    val rowsPerPage = remember { mutableStateOf(20) }

    if (jobList.isEmpty()) return

    Container {
        Row(attrs = { classes("mt-3") }) {
            Column(size = 8) { H1 { Text("Job List") } }
            Column { Button(title = "Clear Completed", color = Color.Info) { viewModel.clearJobs() } }
        }

        Table(
                pagination = Table.OffsetPagination(
                        data = jobList.sortedByDescending { it.startedAt },
                        entriesPerPageLimit = rowsPerPage
                ),
                stripedRows = true,
                fixedHeader = Table.FixedHeaderProperty(
                        topSize = 50.px,
                        zIndex = ZIndex(1000)
                )
        ) { _, job ->
            column("Job ID") { Text(job.jobId) }
            column("Progress") { Text("${job.progress} %") }
            column("Status") { Text(job.status.name) }
            column("Started At") { Text(job.startedAt) }
            column("File") { Text(job.file.fileName) }
            column("") {
                if (job.status in listOf(InProgress, Queued)) {
                    Button(title = "Cancel", color = Color.Danger) {
                        viewModel.cancelJob(job.jobId)
                    }
                } else {
                    Text("No Action")
                }
            }
        }
    }
}