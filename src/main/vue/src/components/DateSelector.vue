<template>
        <div class="date-container">
            <div v-for="(item, index) in data" class="date-item"
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
    date.setHours(18,0,0);
    date.setDate(date.getDate() + item.day);
    emits('dateSelected', date);
}

</script>

<style scoped>
.date-container{
    display:flex;
    flex-wrap: wrap;
    row-gap:10px;
}

.date-item{
    background-color: var(--true-blue);
    margin: 0 5px;
    border-radius: 5px;
    padding: 5px 8px;
    color:white;
    cursor: pointer;
    font-size:12px;
}
</style>