package com.mlsdev.mlsdevstore.data.model.error

interface Parser<Input, Output> {
    fun parse(input: Input): Output
}