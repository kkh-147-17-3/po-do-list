<template>
  <v-app-bar :elevation="1">
    <v-app-bar-nav-icon>
      <v-btn 
        icon="mdi-menu"
        color="blue-darken-2">
      </v-btn>
    </v-app-bar-nav-icon>
    <v-app-bar-title>
        Po-do-List
    </v-app-bar-title>
    <template v-slot:append>
      <v-btn 
        icon="mdi-account"
        color="blue-darken-2">
      </v-btn>
    </template>
  </v-app-bar>
</template>

<script lang="ts" setup>
  //
</script>
