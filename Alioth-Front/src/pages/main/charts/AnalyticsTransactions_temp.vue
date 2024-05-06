<script setup>
const statistics = [
  {
    title: 'Sales',
    stats: '245k',
    icon: 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTEwLjU0IDE0LjUzTDguNDEgMTIuNGwtMS4wNiAxLjA2bDMuMTggMy4xOGw2LTZsLTEuMDYtMS4wNnpNMTIgMjBhNyA3IDAgMCAxLTctN2E3IDcgMCAwIDEgNy03YTcgNyAwIDAgMSA3IDdhNyA3IDAgMCAxLTcgN20wLTE2YTkgOSAwIDAgMC05IDlhOSA5IDAgMCAwIDkgOWE5IDkgMCAwIDAgOS05YTkgOSAwIDAgMC05LTltLTQuMTItLjYxTDYuNiAxLjg2TDIgNS43MWwxLjI5IDEuNTN6TTIyIDUuNzJsLTQuNi0zLjg2bC0xLjI5IDEuNTNsNC42IDMuODZ6Ii8+PC9zdmc+',
    color: 'primary',
  },
  {
    title: 'Customers',
    stats: '12.5k',
    icon: 'ri-group-line',
    color: 'success',
  },
  {
    title: 'Product',
    stats: '1.54k',
    icon: 'ri-macbook-line',
    color: 'warning',
  },
  {
    title: 'Revenue',
    stats: '$88k',
    icon: 'ri-money-dollar-circle-line',
    color: 'info',
  },
]
</script>

<template>
  <VCard title="Transactions">
    <template #subtitle>
      <p class="text-body-1 mb-0">
        <span class="d-inline-block font-weight-medium text-high-emphasis">Total 48.5% Growth</span> <span class="text-high-emphasis">😎</span> this month
      </p>
    </template>

    <template #append>
      <IconBtn class="mt-n5">
        <VIcon
          color="high-emphasis"
          icon="ri-more-2-line"
        />
      </IconBtn>
    </template>

    <VCardText class="pt-10">
      <VRow>
        <VCol
          v-for="item in statistics"
          :key="item.title"
          cols="12"
          sm="6"
          md="3"
        >
          <div class="d-flex align-center gap-x-3">
            <VAvatar
              :color="item.color"
              rounded
              size="40"
              class="elevation-2"
            >
              <VIcon
                size="24"
                :icon="item.icon"
              />
            </VAvatar>

            <div class="d-flex flex-column">
              <div class="text-body-1">
                {{ item.title }}
              </div>
              <h5 class="text-h5">
                {{ item.stats }}
              </h5>
            </div>
          </div>
        </VCol>
      </VRow>
    </VCardText>
  </VCard>
</template>
