<template>
        <div class="select-modal">
            <div v-for="(item, index) in data" 
                :key="index"
                @click="dateClickHandler(item)">
            {{ item.name }}
            </div>
        </div>
</template>

<script setup lang="ts">

interface DateCategory{
    id:number,
    name: string,
    day:number,
}

const emits = defineEmits(['dateSelected']);

const data: Array<DateCategory> = [
    {id:1, name:'오늘',day:0},
    {id:1, name:'내일',day:1},
    {id:1, name:'모레',day:2},
    {id:1, name:'일주일 뒤',day:7},
    {id:1, name:'한달 뒤',day:30},
];


function dateClickHandler(item:DateCategory){
    const date = new Date();
    date.setHours(9,0,0);
    date.setDate(date.getDate() + item.day);
    console.log(date);
    emits('dateSelected', date);
}

</script>

<style scoped>

</style>