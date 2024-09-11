<template>

    <v-data-table
        :headers="headers"
        :items="getMenu"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'GetMenuView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            getMenu : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/getMenus'))

            temp.data._embedded.getMenus.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.getMenu = temp.data._embedded.getMenus;
        },
        methods: {
        }
    }
</script>

