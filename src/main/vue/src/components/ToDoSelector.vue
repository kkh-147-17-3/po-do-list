<template>
    <div class="select-modal">
        <Transition name="parent">
            <div class="parent" v-show="currSelected.parent === null">
                <div v-for="(item, index) in parent" 
                    :key="index"
                    @click="parentClickHandler(item)">
                    {{ item.name }}
                </div>
            <div>직접입력</div>
        </div>
        </Transition>
        <Transition name="child">
            <div class="child" v-show="currSelected.parent !== null">
                <div v-for="(item, index) in child" 
                    :key="index"
                    @click="childClickHandler(item)">
                    {{ item.name }}
                </div>
                <div>직접입력</div>
            </div>
        </Transition>
        
    </div>
</template>



<script setup lang="ts">
import {ref, reactive, watch} from 'vue';

export interface Category {
    id:number,
    name:string,
}

export interface CurrSelected{
    parent: Category | null,
    child: Category | null,
}

const props = defineProps(['opened']);




const parent = [
    { id:1, name:"회사" },
    { id:2, name:"집" },
    { id:3, name:"장보기" },
    { id:4, name:"쇼핑" },
    { id:5, name:"육아" },
    { id:6, name:"은행" },
]

const child: Array<Category> = [
    { id:1, name:"회사" },
    { id:2, name:"집" },
    { id:3, name:"장보기" },
    { id:4, name:"쇼핑" },
    { id:5, name:"육아" },
    { id:6, name:"은행" },
];

const currSelected: CurrSelected = reactive({
    parent: null,
    child: null,
})

const emits = defineEmits(['todoSelected']);

const parentClickHandler = (item:Category) => {
    currSelected.child = null;
    currSelected.parent = item;
}

const childClickHandler = (item:Category) =>{
    currSelected.child = item;
    emits('todoSelected', currSelected);
}

watch(props.opened, ()=>{
    if(props.opened){
        currSelected.parent = null;
        currSelected.child = null;
    }
})

</script>

<style lang="scss" scoped>
.parent-enter-active,
.parent-leave-active {
  transition: all 0.5s ease;
}

.parent-enter-from,
.parent-leave-to {
  transform:translateX(-100%);
  opacity: 0;
}


.child-enter-active,
.child-leave-active {
  transition: all 0.5s ease;
}

.child-enter-to,
.child-leave-from {
  transform:translateX(-100%);
  opacity: 1;
}

.select-modal{
    height:400px; 
    display:flex; 
    overflow-x:hidden;
}
</style>