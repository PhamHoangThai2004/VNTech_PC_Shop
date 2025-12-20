package com.pht.vntechpc.ui.navigation

sealed class Graph (
    val graph: String
) {
    object Auth: Graph("auth_graph")
    object Main: Graph("main_graph")
    object Address: Graph("address_graph")
    object Order: Graph("order_graph")
}