const { createApp } = Vue;

createApp({
    data() {
        return {
            arrayData: [],
            clients: [],
            nombre: "",
            apellido: "",
            email: "",
            search: "",
            id: "",
        }
    },


    created() {
        this.getData("/rest/clients")
    },

    methods: {
        getData(url) {
            axios.get(url).then(response => {
                this.arrayData = response
                this.clients = this.arrayData.data._embedded.clients

            })
        },

        addClient() {
            if (this.nombre.length && this.apellido.length && this.email.length) {
                let clienteNuevo = {
                    firstName: this.nombre,
                    lastName: this.apellido,
                    email: this.email
                }
                console.log(clienteNuevo);
                this.funcionPost(clienteNuevo)
            }
        },

        funcionPost(newClient) {
            axios.post("/rest/clients", newClient).then(() => { this.getData("/rest/clients") })
        },

        funcionDelete(cliente) {
            axios.delete(cliente._links.self.href).then(() => { this.getData("/rest/clients") })
        },

        editClient() {
            if (this.nombre.length && this.apellido.length && this.email.length) {
                let clienteReemplazo = {
                    firstName: this.nombre,
                    lastName: this.apellido,
                    email: this.email
                }
                this.funcionReemplazar(clienteReemplazo)
            }
        },
        mostrarCliente(cliente) {
            this.nombre = cliente.firstName;
            this.apellido = cliente.lastName;
            this.email = cliente.email;
            this.id = cliente._links.self.href
        },

        funcionReemplazar(cliente) {
            axios.put(this.id, cliente).then(() => { this.getData("/rest/clients") })
        },



        clear() {
            this.clients = []
        }

    },

    computed: {

    },

}).mount("#app");


//http://localhost:8080/manager.html?