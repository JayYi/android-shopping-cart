package nextstep.shoppingcart.ui.product.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nextstep.shoppingcart.R
import nextstep.shoppingcart.data.PRODUCT_LIST_MOCK_DATA
import nextstep.shoppingcart.data.Products
import nextstep.shoppingcart.data.ProductsImpl
import nextstep.shoppingcart.domain.model.Product
import nextstep.shoppingcart.ui.component.BasicIconButton
import nextstep.shoppingcart.ui.theme.ShoppingCartTheme

@Composable
internal fun ProductListRoute(
    modifier: Modifier = Modifier,
    onCartClick: () -> Unit = {},
    onProductDetailClick: (Long) -> Unit = {},
) {
    val products: Products = remember { ProductsImpl() }
    val eventListener =
        remember {
            { event: ProductListEvent ->
                when (event) {
                    is ProductListEvent.OnProductCardClick -> {
                        onProductDetailClick(event.productId)
                    }

                    is ProductListEvent.OnCartClick -> {
                        onCartClick()
                    }
                }
            }
        }

    ProductListScreen(
        products = products.getAll(),
        onProductListEvent = eventListener,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductListScreen(
    products: List<Product>,
    onProductListEvent: (ProductListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.product_list_toolbar_title))
                },
                actions = {
                    BasicIconButton(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription =
                            stringResource(
                                id = R.string.shopping_card_content_description,
                            ),
                        onClick = { onProductListEvent(ProductListEvent.OnCartClick) },
                    )
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        ProductListContent(
            products = products,
            onProductListEvent = onProductListEvent,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun ProductListContent(
    products: List<Product>,
    onProductListEvent: (ProductListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding =
            PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 13.dp,
            ),
        modifier = modifier,
    ) {
        items(
            items = products,
            key = { product -> product.id },
        ) { product ->
            ProductCard(
                product = product,
                onCardClick = {
                    onProductListEvent(
                        ProductListEvent.OnProductCardClick(productId = product.id),
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun ProductListScreenPreview() {
    ShoppingCartTheme {
        ProductListScreen(
            products = PRODUCT_LIST_MOCK_DATA,
            onProductListEvent = {},
        )
    }
}
