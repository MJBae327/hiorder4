<template>
    <v-card outlined>
        <v-card-title>
            IsAccept
        </v-card-title>

        <v-card-text>
            <Boolean label="Isaccept" v-model="value.isaccept" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    text
                    @click="isAccept"
            >
                IsAccept
            </v-btn>
            
            <v-btn
                    color="primary"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>
   
    export default {
        name: 'IsAcceptCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.isaccept = false;
        },
        watch: {
        },
        methods: {
            isAccept() {
                this.$emit('isAccept', this.value);
            },
            close() {
                this.$emit('closeDialog');
            },
            change() {
                this.$emit('input', this.value);
            },
        }
    }
</script>

