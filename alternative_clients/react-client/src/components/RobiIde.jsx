import useRobiClient from '../hooks/useRobiClient.jsx';

export default function RobiIde() {

    const robiclient = useRobiClient()

    return <div className="container mt-5">
        {robiclient}
    </div>
}