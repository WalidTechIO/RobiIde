export default function GText({ text }) {
    const width = text.dimension.width < 0 ? 24 : text.dimension.width;
    const height = text.dimension.height < 0 ? 24 : text.dimension.height;

    const fontSize = Math.min(width - 10, height - 10);

    const styles = {
        position: 'absolute',
        width: `${width}px`,
        height: `${height}px`,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        fontSize: `${fontSize}px`,
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        color: text.color,
    };

    return (
        <div style={styles}>
            {text.text}
        </div>
    );
}
