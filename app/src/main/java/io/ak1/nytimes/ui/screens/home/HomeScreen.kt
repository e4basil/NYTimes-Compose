package io.ak1.nytimes.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.ak1.nytimes.R
import io.ak1.nytimes.ui.screens.components.*
import io.ak1.nytimes.ui.screens.navigation.MainDestinations
import io.ak1.nytimes.utility.NetworkState
import io.ak1.nytimes.utility.State

val mainType = mutableStateOf("home")
val tempIndex = mutableStateOf(0)


@Composable
fun HomeScreenComposable(
    listState: LazyListState,
    viewModel: StoriesViewModel,
    navController: NavController
) {
    val stories = viewModel.getStories(mainType.value.toLowerCase())
    val resultList = stories.pagedList.observeAsState(initial = listOf())
    val networkState = stories.networkState.observeAsState(initial = NetworkState.LOADING)
    val refreshState = stories.refreshState.observeAsState(initial = NetworkState.LOADED)
    var swipestate = rememberSaveable {
        mutableStateOf(false)
    }


    // TODO: 24/05/21 add status for 429 Too Many Requests
    Scaffold(
        topBar = { HomeAppBar(navController) }
    ) {
        swipestate.value = refreshState.value == NetworkState.LOADING
        Column(modifier = Modifier.fillMaxSize()) {
            CustomTabBar(listState)
            SwipeRefresh(
                state = rememberSwipeRefreshState(swipestate.value),
                onRefresh = {
                    stories.refresh.invoke()
                },
            ) {
                when (networkState.value.state) {
                    State.RUNNING -> {
                        Shimmer {
                            Column {
                                Card(
                                    backgroundColor = colorResource(id = android.R.color.darker_gray),
                                    elevation = 5.dp, modifier = Modifier
                                        .padding(16.dp, 16.dp)
                                        .requiredHeight(300.dp)
                                        .fillMaxWidth()
                                ) {

                                }

                                Card(
                                    backgroundColor = colorResource(id = android.R.color.darker_gray),
                                    elevation = 5.dp, modifier = Modifier
                                        .padding(16.dp, 0.dp)
                                        .requiredHeight(50.dp)
                                        .fillMaxWidth()
                                ) {

                                }

                                Card(
                                    backgroundColor = colorResource(id = android.R.color.darker_gray),
                                    elevation = 5.dp, modifier = Modifier
                                        .padding(16.dp, 16.dp)
                                        .requiredHeight(50.dp)
                                        .fillMaxWidth()
                                ) {

                                }
                            }
                        }
                    }
                    State.SUCCESS -> {
                        LazyColumn(state = listState) {
                            itemsIndexed(resultList.value) { pos, element ->
                                PostElement(element, viewModel) { result ->
                                    navController.navigate("${MainDestinations.POST_ROUTE}/${result.id}")
                                }
                            }
                        }
                    }
                    State.FAILED -> {
                        PlaceHolder(R.drawable.ic_undraw_not_found_60pq, R.string.internet_error)
                    }
                }

            }
        }

    }
}




